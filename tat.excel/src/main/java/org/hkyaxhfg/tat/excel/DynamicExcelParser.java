package org.hkyaxhfg.tat.excel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态 Excel 解析器.
 *
 * @author: wjf
 * @date: 2022/3/4
 */
public class DynamicExcelParser {

    private static final String EMPTY = "";

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String DECIMAL_FORMAT = "###.####################";

    private final Map<String, Integer> cellIndexCache = new ConcurrentHashMap<>();

    private final Sheet sheet;

    private final String[] dateFormatPattens;

    public DynamicExcelParser(Sheet sheet, String... dateFormatPattens) {
        this.sheet = sheet;
        this.dateFormatPattens = dateFormatPattens;
    }

    /**
     * 获取多层表头的所对应的数据行的数据.
     * @param dataRow 数据行.
     * @param headers 表头数据行.
     * @return 多层表头的所对应的数据.
     */
    public String get(Row dataRow, String[] headers) {
        if (ArrayUtils.isEmpty(headers)) {
            return EMPTY;
        }
        // 获取顶层表头数据行
        Row rootHeaderDataRow = sheet.getRow(0);
        String cacheKey = generateCacheKey(headers);
        if (cellIndexCache.containsKey(cacheKey)) {
            Integer dataRowCellIndex = cellIndexCache.get(cacheKey);
            Cell cell = dataRow.getCell(dataRowCellIndex);
            return cellStringValue(cell, dateFormatPattens);
        }
        // 对于顶层表头, 单元格范围则是整行的单元格, 从header的第一个元素开始查找
        boolean inCache = searchAndRefreshCache(rootHeaderDataRow, rootHeaderDataRow.getFirstCellNum(), rootHeaderDataRow.getLastCellNum(), headers, 0);
        if (cellIndexCache.containsKey(cacheKey) && inCache) {
            Integer dataRowCellIndex = cellIndexCache.get(cacheKey);
            Cell cell = dataRow.getCell(dataRowCellIndex);
            return cellStringValue(cell, dateFormatPattens);
        }
        return EMPTY;
    }


    /**
     * 对于单个单元格: 单元格数据存放在单元格中.
     * 对于合并单元格: 单元格数据存放在单元格为(0, 0)的单元格中.
     *               假设存在一个合并单元格: 3行4列, 那么单元格数据就存放在第1行第1列的这个单元格中,
     *               其它单元格存放的全是空字符串, 这是POI的单元格存放数据的行为.
     *
     * 搜索并更新缓存.
     * @param currentHeaderDataRow 当前表头数据行.
     * @param firstCellIndex 搜索单元格的起始索引.
     * @param lastCellIndex 搜索单元格的结束索引.
     * @param headers 要搜索的表头数组.
     * @param index headers的索引值, 也是代表表头的层数.
     *
     * @return 是否搜索到了此表头.
     */
    private boolean searchAndRefreshCache(Row currentHeaderDataRow, int firstCellIndex, int lastCellIndex, String[] headers, int index) {
        // 当前表头
        String currentHeader = headers[index];

        // 遍历当前表头行, 从给定的开始列和结束列进行查找
        for (int i = firstCellIndex; i <= lastCellIndex; i++) {
            Cell cell = currentHeaderDataRow.getCell(i);
            String cellValue = cell.getStringCellValue();
            // 当前表头和遍历的表头是否长得一样
            if (currentHeader.equals(cellValue)) {
                int columnIndex = cell.getColumnIndex();
                // 如果是表头数组的最后一个则说明找到我们想要的索引值了, 直接放入缓存中
                if (index == headers.length - 1) {
                    cellIndexCache.put(generateCacheKey(headers), columnIndex);
                    return true;
                } else {
                    // 如果不是最后一个则在此基础上继续操作
                    /*
                     * 逻辑说明:
                     *  不是表头数组的最后一个, 则说明还没有找到我们想要的数据, 假设当前层级是第n层,
                     *  那么我们接下来需要查找第n + 1层表头, 也就是对应的header[index + 1]的表头数
                     *  组元素, 一层表头有可能占多行多列, 所以我们需要先判断当前层表头存放表头数据的单
                     *  元格是否是属于一个合并单元格, 然后根据这个合并单元格的信息去获取n + 1层表头的
                     *  信息.
                     */
                    // 判断当前单元格是否是一个合并单元格
                    CellRangeAddress cellRangeAddress = isMergeCellAndGet(cell);

                    if (cellRangeAddress != null) {
                        /*
                         * 逻辑说明:
                         *  既然是一个合并单元格, 那么我们需要获取组成下一层表头(n + 1层)的第一行(即下
                         *  一层表头的数据行), 下一层表头的第一行就是组成当前这个合并单元格的最后一行 + 1,
                         *  然后递归在给定范围的列(下一层表头必定在上一层标头的起始单元格和结束单元格的索引
                         *  范围内)中继续查找目标.
                         */
                        Row nextHeaderDataRow = sheet.getRow(cellRangeAddress.getLastRow() + 1);
                        return searchAndRefreshCache(nextHeaderDataRow, cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn(), headers, index + 1);
                    } else {
                        /*
                         * 逻辑说明:
                         *  当前单元格不是一个合并单元格, 也就是说他是一个独立的单元格, 那么就算表头数组
                         *  没有查找到最后一个元素, 那也说明最后一个表头必须在这个单元格的索引位置, 因为
                         *  是一个独立单元格.
                         */
                        cellIndexCache.put(generateCacheKey(headers), columnIndex);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断当前单元格是否是合并单元格.
     * @param cell 当前单元格.
     * @return 是合并单元格则返回合并单元格范围地址, 否则返回null.
     */
    private CellRangeAddress isMergeCellAndGet(Cell cell) {
        List<CellRangeAddress> cellRangeAddresses = sheet.getMergedRegions();

        if (CollectionUtils.isNotEmpty(cellRangeAddresses)) {
            for (CellRangeAddress cellRangeAddress : cellRangeAddresses) {
                if (cellRangeAddress.isInRange(cell)) {
                    return cellRangeAddress;
                }
            }
        }
        return null;
    }

    /**
     * 生成表头缓存key.
     * @param headers 表头.
     * @return 缓存key.
     */
    private String generateCacheKey(String[] headers) {
        return String.join("->", headers);
    }

    /**
     * 将单元格数据转换为字符串形式.
     * @param cell 单元格.
     * @param dateFormat 日期格式化.
     * @return 字符串形式的数据.
     */
    public static String cellStringValue(Cell cell, String... dateFormat) {
        String value = null;
        if (Objects.isNull(cell)) {
            return EMPTY;
        }
        switch (cell.getCellType()) {
            case STRING:
                // 字符串
                value = cell.getStringCellValue().trim();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 日期
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat format = new SimpleDateFormat(dateFormat.length > 0 ? dateFormat[0] : YYYY_MM_DD_HH_MM_SS);
                    value = format.format(date);
                } else {
                    // 处理科学计数法
                    double temp = cell.getNumericCellValue();
                    DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);
                    value = df.format(temp);
                }
                break;
            case FORMULA:
                //处理数字和公式
                value = String.valueOf(Double.valueOf(cell.getNumericCellValue()));
                value = value.replaceAll("0+?$", EMPTY);
                value = value.replaceAll("[.]$", EMPTY);
                break;
            case BOOLEAN:
                // 布尔
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                value = EMPTY;
                break;
        }
        return value;
    }

}
package cn.yang.util.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author yang
 *
 * Excel 文件读取工具，二维表格，不支持嵌套格式，用于导入、导出表格数据
 */
public interface ExcelHelper {

    enum Type {
        /**
         * .xlsx 文件
         */
        XLSX("xlsx"),
        /**
         * .xls 文件
         */
        XLS("xls");

        private String suffix;

        public String getSuffix() {
            return suffix;
        }

        Type(String suffix) {
            this.suffix = suffix;
        }
    }

    /**
     * 通过输入流生成workbook —— 读取
     *
     * @param inputStream excel输入流
     * @return 返回ExcelHelper实例
     * @throws IOException excel流输入失败时IO异常
     */
    static ExcelHelper getInstance(InputStream inputStream) throws IOException {
        return new ExcelUtil(inputStream);
    }

    /**
     * 通过excel文件类型创建workbook
     *
     * @param type excel文件后缀 {@link Type}
     * @return 返回ExcelHelper实例
     */
    static ExcelHelper getInstance(String type) {
        return new ExcelUtil(type);
    }

    /**
     * 通过excel文件类型创建workbook
     *
     * @return 返回ExcelHelper实例, 默认创建xlsx流
     */
    static ExcelHelper getInstance() {
        return getInstance(Type.XLSX.toString());
    }

    /**
     * 获取sheetIndex工作表数据（第一行为表头，不能为数据行，数据表维度：2）
     *
     * @param sheetIndex sheet 索引，从0开始
     * @param titleNames 列名（第一行数据）的别名，用于实体转换
     * @return 数据列表
     */
    List<Map<String, Object>> read(int sheetIndex, String... titleNames);

    /**
     * 获取sheetIndex工作表数据（第一行为表头，不能为数据行，数据表维度：2）
     *
     * @param sheetIndex sheet 索引，从0开始
     * @param titleName  列名（第一行数据）的别名，用于实体转换
     * @return 数据列表
     */
    List<Map<String, Object>> read(int sheetIndex, Map<Integer, String> titleName);
}

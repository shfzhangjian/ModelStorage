package extractor.DAO.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import extractor.model.errorpath;
@Mapper
@Component
public interface errorpathMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table errorpath
     *
     * @mbg.generated Sun Jul 21 15:22:00 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table errorpath
     *
     * @mbg.generated Sun Jul 21 15:22:00 CST 2019
     */
    int insert(errorpath record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table errorpath
     *
     * @mbg.generated Sun Jul 21 15:22:00 CST 2019
     */
    int insertSelective(errorpath record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table errorpath
     *
     * @mbg.generated Sun Jul 21 15:22:00 CST 2019
     */
    errorpath selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table errorpath
     *
     * @mbg.generated Sun Jul 21 15:22:00 CST 2019
     */
    int updateByPrimaryKeySelective(errorpath record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table errorpath
     *
     * @mbg.generated Sun Jul 21 15:22:00 CST 2019
     */
    int updateByPrimaryKey(errorpath record);
    List<errorpath>	selectByComponent(String componentid);
}
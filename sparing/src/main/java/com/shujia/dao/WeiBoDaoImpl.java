package com.shujia.dao;

import com.shujia.bean.WeiBo;
import com.shujia.util.JDBCUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class WeiBoDaoImpl implements WeiBoDao {
    @Override
    public WeiBo findById(String id) {
        Connection connection = JDBCUtil.getConnection();
        WeiBo weiBo = null;
        try {
            String sql = "select * from weibo where id =?";
            PreparedStatement stat = connection.prepareStatement(sql);
            stat.setString(1, id);

            ResultSet resultSet = stat.executeQuery();

            if (resultSet.next()) {
                weiBo = new WeiBo();
                String content = resultSet.getString("content");
                weiBo.setId(id);
                weiBo.setContent(content);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return weiBo;
    }
}

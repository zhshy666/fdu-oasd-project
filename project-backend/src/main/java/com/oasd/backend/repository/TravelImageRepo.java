package com.oasd.backend.repository;

import com.oasd.backend.domain.TravelImage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TravelImageRepo {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private List<TravelImage> findImages(String sql){
        return jdbcTemplate.query(sql, new RowMapper<TravelImage>(){
            TravelImage image = null;
            @Override
            public TravelImage mapRow(ResultSet resultSet, int i) throws SQLException {
                image = new TravelImage();
                image.setImageId(resultSet.getInt("ImageId"));
                image.setTitle(resultSet.getString("Title"));
                image.setDescription(resultSet.getString("Description"));
                image.setLatitude(resultSet.getDouble("Latitude"));
                image.setLongitude(resultSet.getDouble("Longitude"));
                image.setCityCode(resultSet.getInt("CityCode"));
                image.setCountry_RegionCodeISO(resultSet.getString("Country_RegionCodeISO"));
                image.setPATH(resultSet.getString("Path"));
                image.setUsername(resultSet.getString("Username"));
                image.setContent(resultSet.getString("Content"));
                image.setHeat(resultSet.getInt("Heat"));
                image.setReleasedTime(resultSet.getString("ReleasedTime"));
                image.setAuthor(resultSet.getString("Author"));
                return image;
            }
        });
    }

    public List<TravelImage> findImageByHeat() {
        String sql = "select * from travelimage order by Heat desc limit 5";
        return findImages(sql);
    }

    public List<TravelImage> findImageByTime() {
        String sql = "select * from travelimage order by ReleasedTime desc limit 6";
        return findImages(sql);
    }

    public TravelImage findImageById(int imageId) {
        String sql = "select * from travelimage where ImageID = '" + imageId +"'";
        List<TravelImage> imageList = findImages(sql);
        return imageList.isEmpty() ? null : imageList.get(0);
    }

    public List<TravelImage> findImagesBySearchInput(String input, String scope, String rule) {
        String sql = "select * from travelimage where " + scope +
                " like '%" + input +
                "%' order by " + rule +
                " desc ";
        return findImages(sql);
    }

    public void insertImage(TravelImage image) {
        String sql = "insert into travelimage values (null, ?, ?, 0, 0, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, image.getTitle(), image.getDescription(), image.getCityCode(), image.getCountry_RegionCodeISO(),
                image.getUsername(), image.getPATH(), image.getContent(), image.getHeat(), image.getReleasedTime(), image.getAuthor());
    }

    public List<TravelImage> findImagesByUsername(String username) {
        String sql = "select * from travelimage where Username = '" +
                username + "' order by ReleasedTime desc";
        return findImages(sql);
    }

    public void deleteImgById(int imageId) {
        String sql = "delete from travelimage where ImageID = '" +
                imageId + "'";
        jdbcTemplate.update(sql);
        System.out.println("Delete success");
    }

    public void modifyImage(TravelImage image, int imageId) {
        String sql = "update travelimage set " +
                "Title = ?, author = ?, Description = ?, CityCode = ?, Country_RegionCodeISO = ?, " +
                "PATH = ?, Content = ?, ReleasedTime = ? where ImageID = ?";
        jdbcTemplate.update(sql, image.getTitle(), image.getAuthor(), image.getDescription(), image.getCityCode(),
                image.getCountry_RegionCodeISO(), image.getPATH(), image.getContent(), image.getReleasedTime(), imageId);
        System.out.println("Update successfully");
    }

    public void updateHeatByImageId(int imageId, int step) {
        String sql = "select * from travelimage where ImageID = '"+imageId+"'";
        List<TravelImage> imageList = findImages(sql);
        assert !imageList.isEmpty();
        int heat = imageList.get(0).getHeat();
        heat+=step;
        String sql2 = "update travelimage set Heat = ? where ImageID = ?";
        jdbcTemplate.update(sql2, heat, imageId);
    }
}

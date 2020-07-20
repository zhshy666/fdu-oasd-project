package com.oasd.backend.service;

import com.oasd.backend.domain.City;
import com.oasd.backend.domain.TravelImage;
import com.oasd.backend.repository.CityRepo;
import com.oasd.backend.repository.CountryRepo;
import com.oasd.backend.repository.FavorRepo;
import com.oasd.backend.repository.TravelImageRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ImageService {
    private TravelImageRepo travelImageRepo;
    private CountryRepo countryRepo;
    private CityRepo cityRepo;
    private FavorRepo favorRepo;

    public ImageService(TravelImageRepo travelImageRepo, CountryRepo countryRepo, CityRepo cityRepo, FavorRepo favorRepo) {
        this.travelImageRepo = travelImageRepo;
        this.countryRepo = countryRepo;
        this.cityRepo = cityRepo;
        this.favorRepo = favorRepo;
    }

    public List<TravelImage> getPopularImages() {
        return travelImageRepo.findImageByHeat();
    }

    public List<TravelImage> getNewestImages() {
        return travelImageRepo.findImageByTime();
    }

    public TravelImage getImageDetail(int imageId) {
        return travelImageRepo.findImageById(imageId);
    }

    public List<TravelImage> getSearchResult(String input, String scope, String rule) {
        return travelImageRepo.findImagesBySearchInput(input, scope, rule);
    }

    public String uploadImg(HttpServletRequest request, String username, String upload) {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        TravelImage image = new TravelImage();

        // get basic info about the image
        String title = params.getParameter("title");
        String author = params.getParameter("author");
        String content = params.getParameter("content");
        String description = params.getParameter("description");
        String countryName = params.getParameter("country");
        String cityName = params.getParameter("city");
        int imageId = -1;

        String ISO = countryRepo.findISOByName(countryName);
        int cityCode = cityRepo.findIdByName(cityName);

        long time = System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String releasedTime = formatter.format(date);

        if (upload.equals("upload") || (upload.equals("modify")&&params.getParameter("modifyImg").equals("true"))) {
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");

            assert file != null;
            String type = file.getContentType();
            assert type != null;
            String suffix = type.substring(6);
            String url = "D:/Personal/Studies/2020summer/PJ/project-frontend/static/travel-images/medium/" + title + time + "." + suffix;
            if (!file.isEmpty()) {
                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(
                        new File(url)))) {
                    byte[] bytes = file.getBytes();

                    stream.write(bytes);
                } catch (Exception e) {
                    return "You failed to upload " + " => " + e.getMessage();
                }
            } else {
                return "You failed to upload " + " because the file was empty.";
            }
            image.setPATH(title + time + "." + suffix);
            System.out.println("new Image");
        }

        // store image info
        image.setTitle(title);
        image.setUsername(username);
        image.setAuthor(author);
        image.setDescription(description);
        image.setContent(content);
        image.setCountry_RegionCodeISO(ISO);
        image.setCityCode(cityCode);
        image.setReleasedTime(releasedTime);
        if (upload.equals("modify") && params.getParameter("modifyImg").equals("false")){
            imageId = Integer.parseInt(params.getParameter("imageId"));
            TravelImage myImg = travelImageRepo.findImageById(imageId);
            image.setPATH(myImg.getPATH());
            System.out.println("modify");
        }
        image.setHeat(0);

        if (upload.equals("modify")){
            travelImageRepo.modifyImage(image, imageId);
        }else {
            travelImageRepo.insertImage(image);
        }

        return "upload successful";
    }

    public List<TravelImage> getUploads(String username) {
        return travelImageRepo.findImagesByUsername(username);
    }

    public void deleteImg(int imageId, String path) {
        // delete
        String imgPath = "D:/Personal/Studies/2020summer/PJ/project-frontend/static/travel-images/medium/" + path;
        File file = new File(imgPath);
        if (file.exists()){
            if (file.delete())
               System.out.println("delete file success");
        }

        travelImageRepo.deleteImgById(imageId);
        // remove the image from favors list
        favorRepo.removeFavorsByImageId(imageId);
    }
}

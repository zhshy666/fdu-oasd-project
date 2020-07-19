package com.oasd.backend.service;

import com.oasd.backend.domain.TravelImage;
import com.oasd.backend.repository.TravelImageRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ImageService {
    private TravelImageRepo travelImageRepo;

    public ImageService(TravelImageRepo travelImageRepo) {
        this.travelImageRepo = travelImageRepo;
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

    public String uploadImg(HttpServletRequest request, int id, String upload) {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        TravelImage image = new TravelImage();

        // get basic info about the image
        String title = params.getParameter("title");
        String author = params.getParameter("author");
        String content = params.getParameter("content");
        String description = params.getParameter("description");
        String country = params.getParameter("country");
        String city = params.getParameter("city");

        long time = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(time);

        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");


        assert file != null;
        String type = file.getContentType();
        assert type != null;
        String suffix = type.substring(6);
        String url = "E://" + title + time + "." + suffix;
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


        return "upload successful";
    }
}

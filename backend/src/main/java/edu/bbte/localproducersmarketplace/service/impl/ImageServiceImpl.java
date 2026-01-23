package edu.bbte.localproducersmarketplace.service.impl;

import edu.bbte.localproducersmarketplace.exception.ImageRepositoryException;
import edu.bbte.localproducersmarketplace.exception.ImageServiceException;
import edu.bbte.localproducersmarketplace.model.Image;
import edu.bbte.localproducersmarketplace.repository.ImageRepository;
import edu.bbte.localproducersmarketplace.repository.MinioImageRepository;
import edu.bbte.localproducersmarketplace.service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final MinioImageRepository imageDao;

    public ImageServiceImpl(ImageRepository imageRepository, MinioImageRepository imageDao) {
        this.imageRepository = imageRepository;
        this.imageDao = imageDao;
    }

    @Override
    public Optional<Image> findById(long id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image save(Image image, MultipartFile file, String fileName) {
        String imageUrl;
        try {
            imageUrl = imageDao.save(file, fileName);
        } catch (ImageRepositoryException e) {
            throw new ImageServiceException("Image could not be saved", e);
        }
        image.setName(imageUrl);
        return imageRepository.saveAndFlush(image);
    }

    @Override
    public Image update(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Image> img = imageRepository.findById(id);
        img.ifPresent(image -> imageDao.delete(image.getName()));
        imageRepository.deleteById(id);
    }

    @Override
    public Resource download(String path) {
        return imageDao.getImage(path);
    }
}

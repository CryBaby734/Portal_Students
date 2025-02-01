package org.example.studentportal.service;

import org.example.studentportal.modul.News;
import org.example.studentportal.repository.NewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    // Создание новости
    @Transactional
    public News createNews(News news) {
        news.setPublishedAt(LocalDate.now()); // Устанавливаем текущую дату
        return newsRepository.save(news);
    }

    // Обновление новости
    @Transactional
    public News updateNews(Long id, News updatedNews) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Новость с ID " + id + " не найдена."));

        existingNews.setTitle(updatedNews.getTitle());
        existingNews.setContent(updatedNews.getContent());
        existingNews.setPublishedAt(updatedNews.getPublishedAt());

        return newsRepository.save(existingNews);
    }

    // Удаление новости
    @Transactional
    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new IllegalArgumentException("Новость с ID " + id + " не найдена.");
        }
        newsRepository.deleteById(id);
    }

    // Получение всех новостей
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    // Получение новости по ID
    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Новость с ID " + id + " не найдена."));
    }
}

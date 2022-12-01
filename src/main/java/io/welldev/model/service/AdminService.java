package io.welldev.model.service;

import io.welldev.model.entity.Admin;
import io.welldev.model.repository.AdminRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
@Transactional
public class AdminService {
    public final AdminRepo adminRepo;

    public void save(Admin admin) {
        adminRepo.save(admin);
    }

    public void flush() {
        adminRepo.flush();
    }

    public void saveAndFlush(Admin admin) {
        adminRepo.saveAndFlush(admin);
    }

    //
    public void saveAll(List<Admin> adminsList) {
        adminRepo.saveAll(adminsList);
    }

    public void saveAllAndFlush(List<Admin> adminsList) {
        adminRepo.saveAllAndFlush(adminsList);
    }

    public Admin findById(Long id) {
        return adminRepo.findById(id).get();
    }

    public List<Admin> findAllById() {
        return adminRepo.findAll();
    }

    public void deleteById(Long id) {
        adminRepo.deleteById(id);
    }

    public void delete(Admin admin) {
        adminRepo.delete(admin);
    }

    public void deleteAllById(List<Long> ids) {
        adminRepo.deleteAllById(ids);
    }

    public void deleteAll(List<Admin> admins) {
        adminRepo.deleteAll(admins);
    }

    public void deleteAll() {
        adminRepo.deleteAll();
    }
}

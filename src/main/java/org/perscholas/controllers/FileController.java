package org.perscholas.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.perscholas.models.Student;
import org.perscholas.services.FileService;
import org.perscholas.services.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final StudentService studentService;

    @GetMapping("/uploadform")
    public String uploadForm(){

        log.info("**** Accessing uploadform page ****");
        return "uploadform";
    }

    @PostMapping("/uploadfile")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Principal principal){

        log.info("User " + principal.getName() + " uploaded the file \"" + file.getOriginalFilename() + "\"");

        fileService.uploadFile(file);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename());
        redirectAttributes.addFlashAttribute("filename", "fileupload" + File.separator +file.getOriginalFilename());

        return "redirect:/uploadform";

    }

    @PostMapping("/uploadStudentPicture")
    public String uploadStudentPicture(@RequestParam("file") MultipartFile file, Principal principal) {
        var optStudent = studentService.findStudentByUsername(principal.getName());
        if(optStudent.isPresent()) {
            Student student = optStudent.get();
            studentService.addImageToStudent(file, student.getEmail());
            return "redirect:/student";
        } else {
            throw new RuntimeException("No student found.");
        }
    }

}
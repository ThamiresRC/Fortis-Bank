package fiap.com.br.ApiJava.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController { //1°passo criação do projeto

    @GetMapping("/")
    public String index() {
        return "Banco Digital API - Integrantes: Henrique Maciel, Thamires Ribeiro, Fortis Bank";
    }
}
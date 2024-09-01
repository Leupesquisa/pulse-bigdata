package com.demo.pulse.model;

// @author Leu A. Manuel
// @see https://github.com/Leupesquisa
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data                       // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor          // Gera um construtor sem argumentos
@AllArgsConstructor         // Gera um construtor com todos os argumentos
public class User {

    @Id
    private String id;
    private String username;
    private String password;

    // Lombok gera automaticamente os métodos getters, setters, toString, equals, e hashCode
}


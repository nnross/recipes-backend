package com.example.recipe.instructions;

import jakarta.persistence.*;

/**
 * Table for instructions.
 */
@Entity
@Table(name="instruction")
@SuppressWarnings("unused")
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instruction_id", nullable = false, updatable = false, unique = true)
    private int id;

    @Column(name = "instruction_body", nullable = false)
    private String body;

    public Instruction(int id, String body) {
        this.id = id;
        this.body = body;
    }

    public Instruction(String body) {
        this.body = body;
    }

    public Instruction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

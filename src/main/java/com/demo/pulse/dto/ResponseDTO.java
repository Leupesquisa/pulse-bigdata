package com.demo.pulse.dto;

public record ResponseDTO<T>(String message, T data) { }

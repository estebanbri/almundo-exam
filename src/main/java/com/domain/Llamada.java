package com.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Llamada {

    private Integer duracion;

    private static final Integer MIN_SEGUNDOS = 5;
    private static final Integer MAX_SEGUNDOS = 10;

    public Llamada(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getDuracion() {
        return duracion;
    }

    // retorna lista de llamadas aleatorias
    public static List<Llamada> llamadasAleatorias(Integer cantidadLlamadas) {
        List<Llamada> llamadas = new ArrayList<>();
        IntStream.range(0, cantidadLlamadas)
                 .forEach(i -> llamadas.add(new Llamada(ThreadLocalRandom.current().nextInt(MIN_SEGUNDOS, MAX_SEGUNDOS + 1))));
        return llamadas;
    }

    @Override
    public String toString() {
        return "Llamada{" +
                "duracion=" + duracion +
                '}';
    }
}

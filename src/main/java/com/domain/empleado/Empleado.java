package com.domain.empleado;

import com.domain.Llamada;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Empleado implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Empleado.class);

    private TipoEmpleado tipoEmpleado;

    private Disponibilidad disponibilidad;

    private ConcurrentLinkedDeque<Llamada> llamadasEntrantes;

    private static AtomicInteger cantidadLlamadasFinalizadas;

    public Empleado(TipoEmpleado employeeType) {
        this.tipoEmpleado = employeeType;
        this.disponibilidad = Disponibilidad.DISPONIBLE;
        this.llamadasEntrantes = new ConcurrentLinkedDeque<>();
        cantidadLlamadasFinalizadas = new AtomicInteger(0);
    }

    public TipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    public synchronized Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    private synchronized void setDisponibilidad(Disponibilidad disponibilidad) {
        logger.info("Empleado {} cambio su estado a  {}" ,Thread.currentThread().getName(), disponibilidad);
        this.disponibilidad = disponibilidad;
    }

    public static AtomicInteger getCantidadLlamadasFinalizadas() {
        return cantidadLlamadasFinalizadas;
    }

    // Encola llamada que viene del dispatcher para ser atendida por algun empleado que este disponible
    public synchronized void atender(Llamada llamada) {
        this.llamadasEntrantes.add(llamada);
    }

    @Override
    public void run() {
        while (true) {
            if (!this.llamadasEntrantes.isEmpty()) {
                Llamada llamada = this.llamadasEntrantes.poll();
                this.setDisponibilidad(Disponibilidad.OCUPADO);
                logger.info("Empleado {} estara ocupado por {} segundos",Thread.currentThread().getName(),llamada.getDuracion());
                try {
                    TimeUnit.SECONDS.sleep(llamada.getDuracion());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.setDisponibilidad(Disponibilidad.DISPONIBLE);
                cantidadLlamadasFinalizadas.incrementAndGet();
                logger.info("Empleado {} termino con una llamada de  {} segundos",Thread.currentThread().getName(), llamada.getDuracion());
            }
        }
    }

}
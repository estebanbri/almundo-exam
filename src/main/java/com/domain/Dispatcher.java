package com.domain;


import com.domain.empleado.Empleado;
import com.domain.estrategia.EstrategiaRecepcionLlamadas;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher implements Runnable {

    private static final Integer CANT_THREADS = 10;

    private ExecutorService threadPool;

    private ConcurrentLinkedDeque<Empleado> empleados;

    private ConcurrentLinkedDeque<Llamada> llamadasEntrantes;

    private EstrategiaRecepcionLlamadas estrategiaRecepcionLlamadas;

    public Dispatcher(List<Empleado> empleados) {
        this.empleados = new ConcurrentLinkedDeque<>(empleados);
        this.estrategiaRecepcionLlamadas = new EstrategiaRecepcionLlamadas();
        this.llamadasEntrantes = new ConcurrentLinkedDeque<>();
        this.threadPool = Executors.newFixedThreadPool(CANT_THREADS);
    }

    // Encola las llamadas entrantes con el fin de ser procesada por algun empleado cuando se encuentre disponible
    public synchronized void dispatchCall(Llamada llamada) {
        this.llamadasEntrantes.addLast(llamada);
    }


    public synchronized void comenzarTrabajarEmpleados() {
        empleados.forEach(empleado -> this.threadPool.execute(empleado::run));
    }

    @Override
    public void run() {
        while (true) {
            if (this.llamadasEntrantes.isEmpty()) {
                continue;
            } else {

                // Obtiene empleado desocupado teniendo en cuenta la logica de seleccion entre ellos
                Empleado empleado = this.estrategiaRecepcionLlamadas.encontrarEmpleadoDisponible(this.empleados);
                if (empleado == null) {
                    continue;
                }

                // Desencola de cola de llamadas entrantes
                Llamada llamada = this.llamadasEntrantes.poll();

                empleado.atender(llamada);
            }
        }
    }

}

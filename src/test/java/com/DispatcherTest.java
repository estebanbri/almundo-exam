package com;

import com.empleado.Empleado;
import com.empleado.TipoEmpleado;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


public class DispatcherTest {

    List<Empleado> empleados;
    Dispatcher dispatcher;

    @Before
    public void init(){
        empleados = Arrays.asList(
                new Empleado(TipoEmpleado.OPERADOR),
                new Empleado(TipoEmpleado.SUPERVISOR),
                new Empleado(TipoEmpleado.DIRECTOR));

        dispatcher = new Dispatcher(empleados);
    }

    @Test
    public void LLAMADAS_ENTRANTES_10() throws InterruptedException {

        final int CANT_LLAMADAS = 10;

        dispatcher.comenzarTrabajarEmpleados();

        sleep(1);
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        threadPool.execute(dispatcher::run);
        sleep(1);

        List<Llamada> llamadas = Llamada.llamadasAleatorias(CANT_LLAMADAS);

        llamadas.forEach(llamada -> {
            dispatcher.dispatchCall(llamada);
            sleep(1);
        });

        threadPool.awaitTermination(10 * 5, TimeUnit.SECONDS);
        assertThat(Empleado.getCantidadLlamadasFinalizadas().get() , is(equalTo(CANT_LLAMADAS)));
    }

    private static void sleep(int segundos){
        try {
            TimeUnit.SECONDS.sleep(segundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
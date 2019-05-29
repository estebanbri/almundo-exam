package com.domain.estrategia;

import com.domain.empleado.Disponibilidad;
import com.domain.empleado.Empleado;
import com.domain.empleado.TipoEmpleado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EstrategiaRecepcionLlamadas {

    private static final Logger logger = LoggerFactory.getLogger(EstrategiaRecepcionLlamadas.class);

    public Empleado encontrarEmpleadoDisponible(Collection<Empleado> empleados) {
        List<Empleado> empleadosDisponibles = empleados.stream().filter(empleado -> empleado.getDisponibilidad() == Disponibilidad.DISPONIBLE).collect(Collectors.toList());
        Optional<Empleado> empleado = empleadosDisponibles.stream().filter(e -> e.getTipoEmpleado() == TipoEmpleado.OPERADOR).findAny();
        if (!empleado.isPresent()) {
            empleado = empleadosDisponibles.stream().filter(e -> e.getTipoEmpleado() == TipoEmpleado.SUPERVISOR).findAny();
            if (!empleado.isPresent()) {
                empleado = empleadosDisponibles.stream().filter(e -> e.getTipoEmpleado() == TipoEmpleado.DIRECTOR).findAny();
                if (!empleado.isPresent()) {
                    return null;
                }
            }
        }
        logger.info("Empleado de tipo {} DISPONIBLE", empleado.get().getTipoEmpleado());
        return empleado.get();
    }
}
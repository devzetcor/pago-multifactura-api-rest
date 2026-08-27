package com.davivienda.sv.app.util;

public enum UserStatus {
    NUEVO(0, "Usuario nuevo"),
    ACTIVO(1, "Usuario activo"),
    INACTIVO(2, "Usuario inactivo"),
    BLOQUEADO(3, "Usuario bloqueado"),
    PENDIENTE(4, "Usuario pendiente"),
    ELIMINADO(5, "Usuario eliminado"),
    POR_AUTORIZAR(100, "Por autorizar");

    private final int id;
    private final String descripcion;

    UserStatus(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static String fromId(int id) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getId() == id) {
                return status.getDescripcion();
            }
        }
        return "Estado desconocido";
    }
}

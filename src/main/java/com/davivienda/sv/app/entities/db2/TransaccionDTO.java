package com.davivienda.sv.app.entities.db2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransaccionDTO {
	private Long idTransaccion;
	private Integer enrolamientoColectorId;
	private LocalDateTime fechaCreacion;
	private BigDecimal montoTotal;
	private String estado;
	private String usuarioCreacion;
	private LocalDateTime fechaAprobacion;
	private String usuarioAprobacion;
	private Long idColector;
	private String cuentaAbono;
	private String tipoCuentaAbono;
	private String cuentaCargo;
	private String tipoCuentaCargo;
	private String nombreCategoria;
	private String nombreColector;
	private String cuentaContable;
	private Long categoria;
	private Long empresa;
	private Boolean requiresUserSignature;
	private Boolean readOnly;
	private String motivoRechazo;
	private Boolean aprobo;
	private Boolean rechazo;
	private List<FacturaTransaccion> facturas;
	private String transactionDetails;
}

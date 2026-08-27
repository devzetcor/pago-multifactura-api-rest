package com.davivienda.sv.app.controllers;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.dto.AutorizacionRequestDto;
import com.davivienda.sv.app.services.datasource.ConsultaAutorizacion;
import com.davivienda.sv.app.services.validator.JWTValidator;


@RestController
@RequestMapping("/colecturia/autorizacion")
public class ConsultaAutorizacionController {

    private final ConsultaAutorizacion consultaAutorizacion;
	@Autowired
	private JWTValidator validJWT;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(validJWT);
	}
    public ConsultaAutorizacionController(ConsultaAutorizacion consultaAutorizacion) {
        this.consultaAutorizacion = consultaAutorizacion;
    }

    @PostMapping("/verificar")
    public ResponseEntity<?> verificarAutorizacion(
        @RequestBody Request<AutorizacionRequestDto> request
    ) {
        try {
            consultaAutorizacion.checkTransactionAuthorization(request.getBody().getIdTransaccion());
            return ResponseEntity.ok("ok");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + ex.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> postMethodName(
        @RequestBody Request<AutorizacionRequestDto> request
    ) {
        try {
            consultaAutorizacion.saveTransactionAuthorization(14, "jwilfredo");
            return ResponseEntity.ok("ok");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } 
    }
    

}

package com.davivienda.sv.app.services;

import com.davivienda.sv.app.dto.EnrolamientoColectorDTO;
import com.davivienda.sv.app.entities.db2.EnrolamientoColector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrolamientoColectorService {
 
    @Autowired
    private EnrolamientoColectorRestClient enrolamientoColectorRestClient;
    
    
    /**
     * Guarda una lista de enrolamientos en la base de datos
     */
  
    public List<EnrolamientoColector> guardarEnrolamientos(List<EnrolamientoColectorDTO> enrolamientosDTO) {
        List<EnrolamientoColector> colectors = enrolamientosDTO.stream()
                        .map(enrolamiento -> {
                            EnrolamientoColector enrolamientoColector = new EnrolamientoColector();
                            enrolamientoColector.setIdAtributo(enrolamiento.getIdAtributo());
                            enrolamientoColector.setIdColector(enrolamiento.getIdColector());
                            enrolamientoColector.setIdEmpresa(enrolamiento.getIdEmpresa());
                            enrolamientoColector.setValor(enrolamiento.getValor());
                            enrolamientoColector.setDescripcion(enrolamiento.getDescripcion());
                            return enrolamientoColector;
                        }).collect(Collectors.toList());
        colectors = enrolamientoColectorRestClient.saveAll(colectors);
        return colectors;
    }
    
    /**
     * Consulta enrolamientos por idEmpresa e idColector
     */

    public List<EnrolamientoColectorDTO> eliminarColectores(List<Integer> colectoresIds) {
    	List<EnrolamientoColector> enrolamientoColectors =  enrolamientoColectorRestClient.findAllByIds(colectoresIds);
//        Iterable<Long> ids = colectoresIds.stream().map(e -> e.longValue()).collect(Collectors.toList());
        List<Long> ids = colectoresIds.stream().map(Integer::longValue).collect(Collectors.toList());

        enrolamientoColectorRestClient.deleteAllById(ids);
        return enrolamientoColectors.stream()
                                    .map(e -> {
                                        EnrolamientoColectorDTO dto = new EnrolamientoColectorDTO();
                                        dto.setDescripcion(e.getDescripcion());
                                        dto.setIdAtributo(e.getIdAtributo());
                                        dto.setIdColector(e.getIdColector());
                                        dto.setIdEmpresa(e.getIdEmpresa());
                                        dto.setValor(e.getValor());
                                        return dto;
                                    }).collect(Collectors.toList());
    }

    public List<EnrolamientoColectorDTO> editarColectores(List<EnrolamientoColectorDTO> colectorDTOs) {
        List<EnrolamientoColectorDTO> colectors = new ArrayList<>();
        if(colectorDTOs.isEmpty()) return colectors;

        List<Integer> ids = colectorDTOs.stream()
                                        .map(e -> e.getId())
                                        .map(e -> e.intValue())
                                        .collect(Collectors.toList());

        List<EnrolamientoColector> colectorsEntities=enrolamientoColectorRestClient.findAllByIds(ids);

        colectorsEntities.forEach(colector -> {
            Optional<EnrolamientoColectorDTO> currentDtoOpt = colectorDTOs.stream()
                                                            .filter(e -> e.getId().equals(colector.getId()))
                                                            .findFirst();
            if(currentDtoOpt.isPresent()) {
                EnrolamientoColectorDTO currentDto = currentDtoOpt.get();

                colector.setValor(currentDto.getValor());
                colector.setDescripcion(currentDto.getDescripcion());
            }
        });

        colectorsEntities=  enrolamientoColectorRestClient.saveAll(colectorsEntities);

        return colectorsEntities.stream()
                                    .map(e -> {
                                        EnrolamientoColectorDTO dto = new EnrolamientoColectorDTO();
                                        dto.setDescripcion(e.getDescripcion());
                                        dto.setIdAtributo(e.getIdAtributo());
                                        dto.setIdColector(e.getIdColector());
                                        dto.setIdEmpresa(e.getIdEmpresa());
                                        dto.setValor(e.getValor());
                                        return dto;
                                    }).collect(Collectors.toList());
    }

	public List<EnrolamientoColectorDTO> consultarPorEmpresaYColector(Long idEmpresa, Long idColector) {
		List<EnrolamientoColector> enrolamientos = enrolamientoColectorRestClient.findByIdEmpresaAndIdColector(idEmpresa, idColector);
		List<EnrolamientoColectorDTO> resultado = new ArrayList<>();

		for (EnrolamientoColector entidad : enrolamientos) {
			EnrolamientoColectorDTO dto = convertirEntidadADto(entidad);
			resultado.add(dto);
		}

		return resultado;
	}
    
    /**
     * Convierte Entidad a DTO
     */
    private EnrolamientoColectorDTO convertirEntidadADto(EnrolamientoColector entidad) {
        EnrolamientoColectorDTO dto = new EnrolamientoColectorDTO();
        dto.setId(entidad.getId());
        dto.setIdEmpresa(entidad.getIdEmpresa());
        dto.setIdColector(entidad.getIdColector());
        dto.setIdAtributo(entidad.getIdAtributo());
        dto.setValor(entidad.getValor());
        dto.setDescripcion(entidad.getDescripcion());
        return dto;
    }
}
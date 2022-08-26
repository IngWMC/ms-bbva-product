package com.nttdata.bbva.product.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product {
	@Id
	private String id;
	@NotEmpty(message = "El campo name es requerido.")
	@NotEmpty(message = "El campo productTypeId es requerido.")
	private String productTypeId;
	private ProductType productType;
	private String name;
	@NotEmpty(message = "El campo shortName es requerido.")
	private String shortName;
	@NotEmpty(message = "El campo isMonthlyMaintenance es requerido.")
	@Pattern(regexp = "^true$|^false$", message = "En el campo isMonthlyMaintenance, sólo está permitido los siguientes valores: true or false")
	private String isMonthlyMaintenance; // Libre de mantenimiento mensual
	@NotNull(message = "El campo maximumLimitMonthlyMovements es requerido.")
	private Integer maximumLimitMonthlyMovements; // Límite máximo de movimientos mensuales
}

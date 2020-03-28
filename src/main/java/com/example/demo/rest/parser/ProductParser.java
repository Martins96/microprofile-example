package com.example.demo.rest.parser;

import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.microprofile.config.spi.Converter;

import com.example.demo.rest.vo.ProductVO;

public class ProductParser implements Converter<ProductVO> {

	@Override
	public ProductVO convert(String value) {
		ProductVO product = new ProductVO();
		String[] splittedValues = value.split(";");
		for (String v : splittedValues) {
			String[] map = v.split(":", 2);
			if (map == null || map.length != 2)
				continue;
			switch (map[0]) {
				case "id":
					Integer id = NumberUtils.toInt(map[1], -1);
					if (id == -1)
						break;
					product.setId(id);
					break;
				case "name":
					product.setName(map[1]);
					break;
	
				case "category":
					product.setCategory(map[1]);
					break;
	
				case "price":
					BigDecimal price = new BigDecimal(map[1]);
					product.setPrice(price);
					break;
			}
		}
		return product;
	}

}

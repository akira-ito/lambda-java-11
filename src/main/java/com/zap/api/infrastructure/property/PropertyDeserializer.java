package com.zap.api.infrastructure.property;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.zap.api.domain.property.Address;
import com.zap.api.domain.property.BusinessType;
import com.zap.api.domain.property.GeoLocation;
import com.zap.api.domain.property.ListingStatusType;
import com.zap.api.domain.property.ListingType;
import com.zap.api.domain.property.Location;
import com.zap.api.domain.property.PrecisionType;
import com.zap.api.domain.property.PricingInfos;
import com.zap.api.domain.property.Property;

public class PropertyDeserializer extends StdDeserializer<Property> {
	private static final long serialVersionUID = -2128296545653249624L;

	protected PropertyDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Property deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectCodec codec = parser.getCodec();
		JsonNode node = codec.readTree(parser);

		int usableAreas = node.get("usableAreas").asInt();
		ListingType listingType = ListingType.fromString(node.get("listingType").asText());
		LocalDateTime createdAt = LocalDateTime.parse(node.get("createdAt").asText(), DateTimeFormatter.ISO_DATE_TIME);
		ListingStatusType listingStatus = ListingStatusType.fromString(node.get("listingStatus").asText());
		String id = node.get("id").asText();
		Integer parkingSpaces = Optional.ofNullable(node.get("parkingSpaces")).map(n -> n.asInt()).orElse(null);
		LocalDateTime updatedAt = LocalDateTime.parse(node.get("updatedAt").asText(), DateTimeFormatter.ISO_DATE_TIME);
		boolean owner = node.get("owner").asBoolean();
		List<String> images = StreamSupport.stream(node.withArray("images").spliterator(), false)
				.map(imageNode -> imageNode.asText()).collect(Collectors.toList());
		int bathrooms = node.get("bathrooms").asInt();
		int bedrooms = node.get("bedrooms").asInt();

		JsonNode addressNode = node.get("address");
		String city = addressNode.get("city").asText();
		String neighborhood = addressNode.get("neighborhood").asText();

		JsonNode geoLocationNode = addressNode.get("geoLocation");
		PrecisionType precisionType = PrecisionType.fromString(geoLocationNode.get("precision").asText());

		JsonNode locationNode = geoLocationNode.get("location");
		double lon = locationNode.get("lon").asDouble();
		double lat = locationNode.get("lat").asDouble();
		Location location = Location.of(lon, lat);

		GeoLocation geoLocation = GeoLocation.of(precisionType, location);
		Address address = Address.of(city, neighborhood, geoLocation);

		JsonNode pricingInfosNode = node.get("pricingInfos");

		BigDecimal yearlyIptu = Optional.ofNullable(node.get("yearlyIptu")).map(n -> BigDecimal.valueOf(n.asDouble()))
				.orElse(null);
		BigDecimal price = Optional.ofNullable(pricingInfosNode.get("price")).map(n -> BigDecimal.valueOf(n.asDouble()))
				.orElse(null);
		BigDecimal rentalTotalPrice = Optional.ofNullable(pricingInfosNode.get("rentalTotalPrice"))
				.map(n -> BigDecimal.valueOf(n.asDouble())).orElse(null);
		BusinessType businessType = BusinessType.fromString(pricingInfosNode.get("businessType").asText());
		Integer monthlyCondoFee = Optional.ofNullable(pricingInfosNode.get("monthlyCondoFee")).map(n -> n.asInt())
				.orElse(null);
		PricingInfos pricingInfos = PricingInfos.of(yearlyIptu, price, rentalTotalPrice, businessType, monthlyCondoFee);

		Property property = Property.of(usableAreas, listingType, createdAt, listingStatus, id, parkingSpaces,
				updatedAt, owner, images, address, bathrooms, bedrooms, pricingInfos);
//		Property property = Property.of(usableAreas, listingType, createdAt, listingStatus, id, parkingSpaces,
//				updatedAt, owner, images, address, bathrooms, bedrooms, pricingInfos);
		return property;
	}

}

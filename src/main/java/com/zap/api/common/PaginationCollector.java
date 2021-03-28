package com.zap.api.common;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.zap.api.common.Pagination.PartialPagination;
import com.zap.api.common.Pagination.PartialPagination.PartialPaginationBuilder;

public class PaginationCollector<T> implements Collector<T, PartialPaginationBuilder<T>, PartialPagination<T>> {
	
	public static <T> PaginationCollector<T> toPaginationCollector() {
	    return new PaginationCollector<>();
	}

	@Override
	public Supplier<PartialPaginationBuilder<T>> supplier() {
		return PartialPagination::builder;
	}

	@Override
	public BiConsumer<PartialPaginationBuilder<T>, T> accumulator() {
		return PartialPagination.PartialPaginationBuilder::listing;
	}

	@Override
	public BinaryOperator<PartialPaginationBuilder<T>> combiner() {
		return (left, right) -> left.listings(right.build().getListings());
	}

	@Override
	public Function<PartialPaginationBuilder<T>, PartialPagination<T>> finisher() {
		return PartialPagination.PartialPaginationBuilder::build;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Set.of(Characteristics.UNORDERED);
	}

}

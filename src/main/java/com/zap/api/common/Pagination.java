package com.zap.api.common;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Getter
@AllArgsConstructor(staticName = "of")
public class Pagination<T> {
	private int pageNumber;
	private int pageSize;
	private long totalCount;
	private Collection<T> listings;

	public static <T> Pagination<T> of(Page page, long totalCount, Collection<T> listings) {
		return Pagination.of(page.getPageNumber(), page.getPageSize(), totalCount, listings);
	}

	public <DTO> Pagination<DTO> fromDTO(Collection<DTO> listings) {
		return Pagination.of(this.pageNumber, this.pageSize, this.totalCount, listings);
	}

	@Getter
	@Builder
	public static class PartialPagination<PT> {
		private long totalCount;
		@Singular
		private Collection<PT> listings;
	}
}

package com.joyveb.jkpmall.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Saxon
 * @date 2018/7/16
 */
@Data
public class ListInfo<T> {

	int count;
	List<T> rows;
	int totalPages;
	int skip;
	int limit;

	public ListInfo(int totalRows, List<T> rows, int skip, int limit) {
		super();
		init(totalRows, rows, skip, limit);
	}

	public final void init(int totalRows, List<T> rows, int skip, int limit) {
		this.count = totalRows;
		this.rows = rows;
		if (rows != null && !rows.isEmpty()) {
			this.totalPages = totalRows / limit
				+ (totalRows % limit == 0 ? 0 : 1);
		} else {
			this.totalPages = 0;
		}
		this.skip = skip;
		this.limit = limit;
	}

	public ListInfo() {
		super();
		rows = new ArrayList<>();
	}

	public T getItem(int index) {
		if (index >= 0 && index < rows.size()) {
			return rows.get(index);
		} else {
			return null;
		}
	}

	public T firstItem() {
		if (!rows.isEmpty()) {
			return rows.get(0);
		}
		return null;
	}
}

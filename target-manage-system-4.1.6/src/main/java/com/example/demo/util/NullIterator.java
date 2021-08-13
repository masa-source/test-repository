package com.example.demo.util;

import java.util.Iterator;

import com.example.demo.model.TargetComponent;

public class NullIterator implements Iterator<TargetComponent> {

	@Override
	public TargetComponent next() {
		return null;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}

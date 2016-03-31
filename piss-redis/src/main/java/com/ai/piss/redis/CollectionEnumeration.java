package com.ai.piss.redis;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

public class CollectionEnumeration<T> implements Enumeration<T>{
	private Iterator<T> it;

	public CollectionEnumeration(Collection<T> c){
		this(c.iterator());
	}
	
	public CollectionEnumeration(Iterator<T> it){
		this.it=it;
	}

	@Override
	public boolean hasMoreElements() {
		return this.it.hasNext();
	}

	@Override
	public T nextElement() {
		return this.it.next();
	}
}

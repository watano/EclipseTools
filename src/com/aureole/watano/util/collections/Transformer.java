package com.aureole.watano.util.collections;

public interface Transformer<Input, Output> {
	public Output transform(Input input);
}

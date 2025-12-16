package farn.another_alpha_again.option;

public class OptionValue<T> {
	public T value;
	public final Class<T> type;

	public OptionValue(T value, Class<T> type) {
		this.value = value;
		this.type = type;
	}
}

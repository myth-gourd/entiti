package myth.gourd.entiti.innotation;

public @interface ValidateSize 
{
	String condition() default "";
	int min() default 1;
	int max() default 255;
	ValidateSizeType type() default ValidateSizeType.BetweenMinAndMax;
}
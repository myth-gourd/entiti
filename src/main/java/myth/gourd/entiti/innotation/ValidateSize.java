package myth.gourd.entiti.innotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface ValidateSize 
{
	String condition() default "";
	int min() default 1;
	int max() default 255;
}
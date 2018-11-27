package me.bloodybadboy.gamedatabase.di.annotations;

import androidx.lifecycle.ViewModel;
import dagger.MapKey;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD })
@Retention(RUNTIME)
@MapKey
public @interface ViewModelKey {
  Class<? extends ViewModel> value();
}
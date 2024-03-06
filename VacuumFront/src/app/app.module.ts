import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './header/header.component';
import { ToastrModule } from 'ngx-toastr';
import { UsersComponent } from './users/users.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptor/token.interceptor';
import { NewUserComponent } from './new-user/new-user.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ErrorHistoryComponent } from './error-history/error-history.component';
import { AddVacuumComponent } from './add-vacuum/add-vacuum.component';
import { SearchVacuumComponent } from './search-vacuum/search-vacuum.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HeaderComponent,
    UsersComponent,
    NewUserComponent,
    EditUserComponent,
    ErrorHistoryComponent,
    AddVacuumComponent,
    SearchVacuumComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    MatSlideToggleModule,
    MatDatepickerModule,
    BrowserAnimationsModule,
    MatInputModule,
    ToastrModule.forRoot(),
    ReactiveFormsModule // Don't forget to add ReactiveFormsModule to the imports array

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

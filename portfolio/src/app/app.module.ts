import { AppRoutingModule } from './app.routing';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { SignupComponent } from './signup/signup.component';
import { IndividualSignupComponent } from './individual-signup/individual-signup.component';
import { CompanySignupComponent } from './company-signup/company-signup.component';
import { ApiCallerService } from './api-caller.service';

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    IndividualSignupComponent,
    CompanySignupComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [ApiCallerService],
  bootstrap: [AppComponent]
})
export class AppModule { }

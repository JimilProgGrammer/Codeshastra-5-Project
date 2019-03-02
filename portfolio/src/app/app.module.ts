import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { SignupComponent } from './signup/signup.component';
import { IndividualSignupComponent } from './individual-signup/individual-signup.component';
import { CompanySignupComponent } from './company-signup/company-signup.component';

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    IndividualSignupComponent,
    CompanySignupComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

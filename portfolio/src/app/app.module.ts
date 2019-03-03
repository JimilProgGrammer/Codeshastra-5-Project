import { AppRoutingModule } from './app.routing';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
import { AppComponent } from './app.component';
import { SignupComponent } from './signup/signup.component';
import { IndividualSignupComponent } from './individual-signup/individual-signup.component';
import { CompanySignupComponent } from './company-signup/company-signup.component';
import { ApiCallerService } from './api-caller.service';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { NavbarComponent } from './navbar/navbar.component';
import { ShareSummaryComponent } from './share-summary/share-summary.component';
import { PieChartComponent } from './pie-chart/pie-chart.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { TradebookComponent } from './tradebook/tradebook.component';

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    IndividualSignupComponent,
    CompanySignupComponent,
    HomeComponent,
    ProfileComponent,
    NavbarComponent,
    ShareSummaryComponent,
    PieChartComponent,
    NotificationsComponent,
    TradebookComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ChartsModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [ApiCallerService],
  bootstrap: [AppComponent]
})
export class AppModule { }

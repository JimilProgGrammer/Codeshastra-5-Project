import { ProfileComponent } from './profile/profile.component';
import { HomeComponent } from './home/home.component';
import { SignupComponent } from './signup/signup.component';
import { Routes, RouterModule, Router } from '@angular/router';
import { NgModule } from '@angular/core';
import { PieChartComponent } from './pie-chart/pie-chart.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { TradebookComponent } from './tradebook/tradebook.component';

const routes: Routes = [
    { path:'', pathMatch: 'full', redirectTo: 'home' },
    { path:'signup', component: SignupComponent },
    { path:'home', component: HomeComponent },
    { path:'profile/:email_id', component: ProfileComponent },
    { path:'pie', component: PieChartComponent },
    { path:'notifications', component: NotificationsComponent },
    { path:'tradebook', component: TradebookComponent }
]
@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule{}

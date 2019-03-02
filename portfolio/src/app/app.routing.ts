import { HomeComponent } from './home/home.component';
import { SignupComponent } from './signup/signup.component';
import { Routes, RouterModule, Router } from '@angular/router';
import { NgModule } from '@angular/core';

const routes: Routes = [
    { path:'', pathMatch: 'full', redirectTo: 'home' },
    { path:'signup', component: SignupComponent },
    { path:'home', component: HomeComponent }
]
@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule{}
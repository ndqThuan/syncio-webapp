import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/authentication/login/login.component';
import { ForgotpasswordComponent } from './features/authentication/forgotpassword/forgotpassword.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { ChangePasswordComponent } from './features/authentication/changepassword/changepassword.component';

@NgModule({
  declarations: [
    // your components
  ],
  imports: [
    // other modules
    ToastModule,
    DialogModule,
  ],
  // providers, bootstrap, etc.
})
export class YourModule {}
const routes: Routes = [
  {
    path: '',
    title: 'Syncio',
    loadChildren: () =>
      import('./features/user/user.module').then((m) => m.UserModule),
  },
  {
    path: 'admin',
    title: 'Administration | Syncio',
    loadChildren: () =>
      import('./features/admin/admin.module').then((m) => m.AdminModule),
  },
  {
    path: 'login',
    title: 'Login',
    component: LoginComponent,
  },
  {
    path: 'forgot_password',
    title: 'forgot',
    component: ForgotpasswordComponent,
  },
  {
    path: 'reset_password',
    title: 'reset_password',
    component: ChangePasswordComponent,
  },
];

@NgModule({
  declarations: [
    LoginComponent,
    ForgotpasswordComponent,
    ChangePasswordComponent,
  ],
  imports: [
    FormsModule,
    CommonModule,
    BrowserModule,
    RouterModule.forRoot(routes),
    ToastModule,
    DialogModule,
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}

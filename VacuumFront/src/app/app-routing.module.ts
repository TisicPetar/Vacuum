import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { UsersComponent } from './users/users.component';
import { NewUserComponent } from './new-user/new-user.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { ErrorHistoryComponent } from './error-history/error-history.component';
import { AddVacuumComponent } from './add-vacuum/add-vacuum.component';
import { SearchVacuumComponent } from './search-vacuum/search-vacuum.component';

const routes: Routes = [
  {
    path:'login',
    component:LoginComponent,
    pathMatch:'full'
  },
  {
    path:'users',
    component:UsersComponent,
    pathMatch:'full'
  },
  {
    path:'new',
    component:NewUserComponent,
    pathMatch:'full'
  },
  {
    path:'edit',
    component:EditUserComponent,
    pathMatch:'full'
  },
  {
    path:'history',
    component:ErrorHistoryComponent,
    pathMatch:'full'
  },
  {
    path:'addVacuum',
    component:AddVacuumComponent,
    pathMatch:'full'
  },
  {
    path:'searchVacuum',
    component:SearchVacuumComponent,
    pathMatch:'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

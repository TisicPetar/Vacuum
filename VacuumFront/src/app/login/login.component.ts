import { Component, OnInit } from '@angular/core';
import { UserServiceService} from '../service/user-service.service'
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {

  username: string = "";
  password: string = "";
  permisions: any = []

  constructor(private userService: UserServiceService, private toastr:ToastrService, private router:Router){

  }

  onSubmit() {
    localStorage.clear();
    this.userService.login(this.username, this.password).subscribe(
        (response)=>{
          localStorage.setItem('jwt', response.jwt)
          for(const permission of response.permissions){
            localStorage.setItem(permission.name, permission.id)
          }
          this.router.navigate(['/users'])
        },
        (err)=>{
          if (err.status == 401){
            window.alert('Auth Error');
          } else
          window.alert('unauthorized');
        }
    )
  }
}

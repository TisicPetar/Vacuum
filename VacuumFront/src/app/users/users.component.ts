import { Component, OnInit } from '@angular/core';
import { UserServiceService } from '../service/user-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
})

export class UsersComponent implements OnInit{

  permissions: any = localStorage.getItem('permisions')
  users:any = []


  constructor(private userService:UserServiceService, private router:Router){

  }

  checkCreatePermision(){
    if(localStorage.getItem('can_create_users')){
      return true;
    }

    return false;
  }

  checkReadPermition(){

    if(localStorage.getItem('can_read_users')){
      return true;
    }

    return false;
  }

  checkDeletePermition(){
    if(localStorage.getItem('can_delete_users')){
      return true;
    }
    return false;
  }

  checkEditPermition(){
    if(localStorage.getItem('can_update_users')){
      return true;
    }

    return false;
  }

  editUser(id:string, oldname:string){
    localStorage.setItem("editer",id);
    sessionStorage.setItem("odlname", oldname);
    this.router.navigate(['/edit']);
  }

  deleteUser(id:number){
    this.userService.deleteUser(id).subscribe(
      (data) => {

        this.users.length = 0;

        for(const user of data){
          this.users.push(user)
          this.checkEditPermition()
        }
      }
    )
  }

  addUser(){
    this.router.navigate(['/new'])
  }

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe(
      (response) => {
        for(const user of response){
          this.users.push(user)
          this.checkEditPermition()
        }
      }
    )
  }

}

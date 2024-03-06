import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserServiceService } from '../service/user-service.service';
import { Router } from '@angular/router';
import { outputAst } from '@angular/compiler';

@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
})

export class NewUserComponent implements OnInit {

  userForm: FormGroup;

  
  read: boolean = false;
  write: boolean = false;
  delete: boolean = false;
  update: boolean = false;

  search: boolean = false;
  start: boolean = false;
  stop: boolean = false;
  discharge: boolean = false;
  add: boolean = false;
  remove: boolean = false;

  includeArray: any[] = [];

  constructor(private fb: FormBuilder, private userService: UserServiceService, private router:Router) {
    // Initialize the form with validators
    this.userForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  onSubmit() {

    const username = this.userForm.value.username;
    const password = this.userForm.value.password;

    ///redirect
    this.userService.addUser(username,password,this.includeArray).subscribe(
      (response) =>{
        this.router.navigate(['/users'])
      }
    )

  }

  handleIncludeCheck(event: any) {
    if (event.target.checked) {
      this.includeArray.push({id: event.target.value});
      }
  };

}

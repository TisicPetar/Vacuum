import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormsModule } from '@angular/forms';
import { UserServiceService } from '../service/user-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
})

export class EditUserComponent{
  
  userForm: FormGroup;

  read: boolean = false;
  write: boolean = false;
  deleteP: boolean = false;
  update: boolean = false;
  oldname: string = sessionStorage.getItem("odlname") ?? "";

  search: boolean = false;
  start: boolean = false;
  stop: boolean = false;
  discharge: boolean = false;
  add: boolean = false;
  remove: boolean = false;

  includeArray: any[] = [];

  constructor(private fb: FormBuilder, private userService: UserServiceService, private router:Router) {
    this.userForm = this.fb.group({
      username: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  onSubmit() {
    const username = this.userForm.value.username;

    ///redirect
    this.userService.editUser(username,this.includeArray).subscribe(
      (response) =>{
        this.router.navigate(['/users']);
        localStorage.removeItem("editer");
        sessionStorage.removeItem("oldname");
      }
    )

  }

  handleIncludeCheck(event: any): void {
    if (event.target.checked) {
      this.includeArray.push({id: event.target.value});
    }
  };

}

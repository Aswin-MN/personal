import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';

export interface Language {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  // languages: Language[] = [
  //   { value: 'English', viewValue: 'English' },
  //   { value: 'French', viewValue: 'French' },
  //   { value: 'Arabic', viewValue: 'Arabic' }
  // ];

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit() {}

  onLogout() {
    console.log('Logging out');
  }

  onHome() {
    let homeURL = '';
    const route_parts = this.router.url.split('/');
    if (route_parts[2]) {
      homeURL = 'dashboard/' + route_parts[2];
    } else {
      homeURL = '/';
    }
    this.router.navigate([homeURL]);
  }
}

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanySignupComponent } from './company-signup.component';

describe('CompanySignupComponent', () => {
  let component: CompanySignupComponent;
  let fixture: ComponentFixture<CompanySignupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompanySignupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanySignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

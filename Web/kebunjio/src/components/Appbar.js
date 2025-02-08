import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { Link, useNavigate } from 'react-router-dom';
import './Appbar.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useAuth } from '../context/AuthContext';
import Image from 'react-bootstrap/Image';

function Appbar() {
  const {isAdmin, setAuthUser, setIsLoggedIn} = useAuth()
  const navigate = useNavigate()

  const logout = () =>{
    setAuthUser(null)
    setIsLoggedIn(false)
    navigate(`/login`)
  }
  return (
    <div>
      {
        isAdmin?(<Navbar expand="lg" className="custom-navbar">
          <Container>
          <Image src="./logo_appbar.png" style={{width:"50px"}}/>
            <Navbar.Brand as={Link} to="/">KebunJio</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                <Nav.Link as={Link} to="/forum">Forum</Nav.Link>
                <Nav.Link as={Link} to="/admin/events">Event</Nav.Link>
                <Nav.Link as={Link} to="/admin/dashboard">Dashboard</Nav.Link>
                <Nav.Link as={Link} onClick={logout}>Logout</Nav.Link>

              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>):(
              <Navbar expand="lg" className="custom-navbar">
              <Container>
                <Image src="./logo_appbar.png" style={{width:"50px"}}/>
                <Navbar.Brand as={Link} to="/">KebunJio</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                  <Nav className="me-auto">
                    <Nav.Link as={Link} to="/forum">Forum</Nav.Link>
                    <Nav.Link as={Link} to="/events">Event</Nav.Link>
                    <Nav.Link as={Link} to="/user-profile">User Profile</Nav.Link>
                    <Nav.Link as={Link} onClick={logout}>Logout</Nav.Link>
                  </Nav>
                </Navbar.Collapse>
              </Container>
            </Navbar>
        )
      }

    </div>
  );
}

export default Appbar;
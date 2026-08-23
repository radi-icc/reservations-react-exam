import { Route, Routes } from "react-router-dom";

import PublicLayout from "../components/layout/PublicLayout";
import AuthLayout from "../components/layout/AuthLayout";
import AdminLayout from "../components/layout/AdminLayout";
import PrivateRoute from "./PrivateRoute";
import AdminRoute from "./AdminRoute";

import Home from "../pages/public/Home";
import Shows from "../pages/public/Shows";
import ShowDetails from "../pages/public/ShowDetails";
import AffiliateCatalogue from "../pages/public/AffiliateCatalogue";
import NotFound from "../pages/public/NotFound";

import Login from "../pages/auth/Login";
import Register from "../pages/auth/Register";

import Profile from "../pages/member/Profile";
import MyReservations from "../pages/member/MyReservations";
import MyReviews from "../pages/member/MyReviews";
import ProducerDashboard from "../pages/member/ProducerDashboard";
import CriticWorkspace from "../pages/member/CriticWorkspace";
import AffiliateWorkspace from "../pages/member/AffiliateWorkspace";

import Dashboard from "../pages/admin/Dashboard";
import ManageShows from "../pages/admin/ManageShows";
import ManageUsers from "../pages/admin/ManageUsers";
import ManageLocations from "../pages/admin/ManageLocations";
import ManageArtists from "../pages/admin/ManageArtists";
import ManageRepresentations from "../pages/admin/ManageRepresentations";
import ManageReservations from "../pages/admin/ManageReservations";
import ManageReviews from "../pages/admin/ManageReviews";
import ManageRoles from "../pages/admin/ManageRoles";
import ManagePrices from "../pages/admin/ManagePrices";
import ManageLocalities from "../pages/admin/ManageLocalities";
import ManageArtistTypes from "../pages/admin/ManageArtistTypes";
import ManageArtistTypeAssignments from "../pages/admin/ManageArtistTypeAssignments";
import ManageCollaborations from "../pages/admin/ManageCollaborations";
import ManageAffiliatePlans from "../pages/admin/ManageAffiliatePlans";
import ManageApiKeys from "../pages/admin/ManageApiKeys";
import AdminTools from "../pages/admin/AdminTools";

const AppRoutes = () => (
  <Routes>
    <Route element={<PublicLayout />}>
      <Route path="/" element={<Home />} />
      <Route path="/shows" element={<Shows />} />
      <Route path="/shows/:id" element={<ShowDetails />} />
      <Route path="/affiliate-catalogue" element={<AffiliateCatalogue />} />
      <Route path="/profile" element={<PrivateRoute><Profile /></PrivateRoute>} />
      <Route path="/my-reservations" element={<PrivateRoute><MyReservations /></PrivateRoute>} />
      <Route path="/my-reviews" element={<PrivateRoute><MyReviews /></PrivateRoute>} />
      <Route path="/producer" element={<PrivateRoute><ProducerDashboard /></PrivateRoute>} />
      <Route path="/critic" element={<PrivateRoute><CriticWorkspace /></PrivateRoute>} />
      <Route path="/affiliate" element={<PrivateRoute><AffiliateWorkspace /></PrivateRoute>} />
    </Route>

    <Route element={<AuthLayout />}>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
    </Route>

    <Route path="/admin" element={<AdminRoute><AdminLayout /></AdminRoute>}>
      <Route index element={<Dashboard />} />
      <Route path="shows" element={<ManageShows />} />
      <Route path="users" element={<ManageUsers />} />
      <Route path="locations" element={<ManageLocations />} />
      <Route path="artists" element={<ManageArtists />} />
      <Route path="representations" element={<ManageRepresentations />} />
      <Route path="reservations" element={<ManageReservations />} />
      <Route path="reviews" element={<ManageReviews />} />
      <Route path="roles" element={<ManageRoles />} />
      <Route path="prices" element={<ManagePrices />} />
      <Route path="localities" element={<ManageLocalities />} />
      <Route path="artist-types" element={<ManageArtistTypes />} />
      <Route path="artist-type-assignments" element={<ManageArtistTypeAssignments />} />
      <Route path="collaborations" element={<ManageCollaborations />} />
      <Route path="affiliate-plans" element={<ManageAffiliatePlans />} />
      <Route path="api-keys" element={<ManageApiKeys />} />
      <Route path="tools" element={<AdminTools />} />
    </Route>

    <Route path="*" element={<NotFound />} />
  </Routes>
);

export default AppRoutes;

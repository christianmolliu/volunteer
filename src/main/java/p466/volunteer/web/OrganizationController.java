package p466.volunteer.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import p466.volunteer.data.OrganizationRepository;
import p466.volunteer.data.OrganizationUserRepository;
import p466.volunteer.data.UserRepository;
import p466.volunteer.data.UserRolesRepository;
import p466.volunteer.domain.Organization;
import p466.volunteer.domain.OrganizationUser;
import p466.volunteer.domain.User;
import p466.volunteer.domain.UserRole;
import p466.volunteer.security.AuthenticationService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequestMapping("/organization")
public class OrganizationController {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationUserRepository organizationUserRepository;
    private final UserRolesRepository userRolesRepository;
    private final AuthenticationService authenticationService;

    public OrganizationController(OrganizationRepository organizationRepository, UserRepository userRepository, OrganizationUserRepository organizationUserRepository, UserRolesRepository userRolesRepository, AuthenticationService authenticationService) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.organizationUserRepository = organizationUserRepository;
        this.userRolesRepository = userRolesRepository;
        this.authenticationService = authenticationService;
    }

    @ModelAttribute("org")
    public Organization createOrganization() {
        return new Organization();
    }

    @ModelAttribute
    public User getUserInfo(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/create")
    public String getOrganizationCreateForm() {
        return "createOrganization";
    }

    @PostMapping("/create")
    public String processCreateOrganization(@Valid Organization org, Errors errors, Authentication authentication, HttpServletRequest request,
                                            HttpServletResponse response) {
        if (errors.hasErrors()) {
            System.out.println("There were errors in the org.");
            return "createOrganization";
        }
        if (organizationRepository.existsByName(org.getName())) {
            System.out.println("Organization with this name already exists");
            return "createOrganization";
        }
        // Give this user organizer role
        User user = (User) authentication.getPrincipal();
        List<UserRole> userRoles = userRolesRepository.findAllByUserId(user.getId());
        boolean alreadyHasRole = false;
        for (UserRole role : userRoles) {
            if (role.getRole().equals("ROLE_ORGANIZER")) {
                alreadyHasRole = true;
                break;
            }
        }
        if (!alreadyHasRole) {
            // user should get 'ROLE_ORGANIZER'
            UserRole userRole = new UserRole();
            userRole.setRole("ROLE_ORGANIZER");
            userRole.setUserId(user.getId());
            userRolesRepository.save(userRole);
        }

        // Create the org
        organizationRepository.save(org);

        // Add the user to the org
        // At this point, we have an org object but no orgId. Retrieve this
        org = organizationRepository.getByName(org.getName());
        OrganizationUser orgUser = new OrganizationUser();
        orgUser.setUserId(user.getId());
        orgUser.setOrganizationId(org.getId());
        organizationUserRepository.save(orgUser);
        // NEW** authentication service used to refresh roles. Had to look up how to do this
        authenticationService.refreshAuthentication(request, response);

        return "redirect:/organizationHub";
    }

    @PostMapping("/invite")
    public String processOrgInvite(@RequestParam String username, @RequestParam Long orgId, Authentication authentication) throws AccessDeniedException {
        // verify requester has this permission
        User user = (User) authentication.getPrincipal();
        boolean belongsToOrganization = organizationUserRepository.existsByOrganizationIdAndUserId(orgId, user.getId());
        if (!belongsToOrganization) {
            throw new AccessDeniedException("You do not have permission");
        }
        // Verify User exists
        User userToAdd = userRepository.findByUsername(username);
        if (userToAdd == null) {
            System.out.println("User does not exist");
            return "redirect:/organizationHub";
        }
        // Verify User is not in org yet
        Long userId = userToAdd.getId();
        if (organizationUserRepository.existsByOrganizationIdAndUserId(orgId, userId)) {
            System.out.println("User already in organization");
            return "redirect:/organizationHub";
        }
        // Give user "ORGANIZER' if doesn't already have
        List<UserRole> userRoles = userRolesRepository.findAllByUserId(userId);
        boolean alreadyHasRole = false;
        for (UserRole role : userRoles) {
            if (role.getRole().equals("ROLE_ORGANIZER")) {
                alreadyHasRole = true;
                break;
            }
        }
        if (!alreadyHasRole) {
            // user should get 'ROLE_ORGANIZER'
            UserRole userRole = new UserRole();
            userRole.setRole("ROLE_ORGANIZER");
            userRole.setUserId(userId);
            userRolesRepository.save(userRole);
        }

        // Add them to orgUser relationship
        OrganizationUser orgUser = new OrganizationUser();
        orgUser.setUserId(userId);
        orgUser.setOrganizationId(orgId);
        organizationUserRepository.save(orgUser);

        return "redirect:/organizationHub";
    }
}

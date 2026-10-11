package p466.volunteer;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Table("USERS")
@Data
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    // roles couldn't be a normal instance variable because it is not stored in the USERS table
    // online I found this annotation that tells spring that this is not meant to be stored in Users.
    // still useful to be tied to the User object, so I think it fits here.
    @Transient
    private List<UserRole> roles = new ArrayList<>();

    @Id
    private Long id;

    private final String username;
    private final String password;
    private final String fullname;
    private final String street;
    private final String city;
    private final String state;
    private final String zip;
    private final String phoneNumber;

    @PersistenceCreator
    public User(
            Long id,
            String username,
            String password,
            String fullname,
            String street,
            String city,
            String state,
            String zip,
            String phoneNumber
    ) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
    }

    public User(
            String username,
            String password,
            String fullname,
            String street,
            String city,
            String state,
            String zip,
            String phoneNumber
    ) {

        this(null, username, password, fullname,
                street, city, state, zip, phoneNumber);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(UserRole::getRole)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

package baecon.devgames.model.update;

import android.content.Context;

import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import baecon.devgames.DevGamesApplication;
import baecon.devgames.connection.client.DevGamesClient;
import baecon.devgames.connection.client.dto.UserDTO;
import baecon.devgames.database.DBHelper;
import baecon.devgames.model.User;
import baecon.devgames.util.L;
import retrofit.client.Response;

@DatabaseTable(tableName = DBHelper.Tables.USER_UPDATES)
public class UserUpdate extends AbsModelUpdate<User> {

    public UserUpdate() {
        // Empty constructor for ORMLite
    }

    public UserUpdate(User user) {
        super(user);
    }

    /**
     * Create an update that will PUT the user object
     *
     * @param localModelId
     * @param user
     *         The user, where ONLY the fields are populated that you'd like to update. The id is populated
     *         for you.
     */
    public UserUpdate(Long localModelId, User user) {
        super(localModelId, user);
    }

    @Override
    public Response sync(Context context, DBHelper dbHelper, DevGamesClient client) throws Exception {

        Response response;

        switch (getOperation()) {

            case UPDATE:

                UserDTO dto = new UserDTO(getModel());
                L.v("Update for {0}", dto.toString());
                response = client.updateUser(dto, DevGamesApplication.get(context).getLoggedInUser().getId());
                L.d("Update response status : {0}, reason : {1}", response.getStatus(), response.getReason());

                break;

            case CREATE:
                // Fall trough allowed
            case UPDATE_FIELD:
                // Fall trough allowed
            case DELETE:
                throw new RuntimeException("Not implemented");

            default:
                throw new Exception("Operation not recognized: " + String.valueOf(getOperation()));
        }

        return response;
    }

    @Override
    public User getModelFromDb(DBHelper dbHelper) throws SQLException {
        return DBHelper.getUserDao(dbHelper).queryForId(getLocalId());
    }

    @Override
    public String toString() {
        return "UserUpdate: " + (getModel() != null ? getModel().toString() : "null") + " - " +super.toString();
    }
}
